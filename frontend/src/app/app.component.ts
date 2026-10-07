import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface Account { accountNumber:string; accountHolderName:string; ownerUsername:string; balance:number; status:string; }
interface LoginResponse { accessToken:string; tokenType:string; }

@Component({
 selector:'app-root', standalone:true, imports:[CommonModule,FormsModule],
 template: `
 <div class="app">
  <header class="topbar"><div><div class="eyebrow">CUSTODY OPERATIONS</div><h1>Banking Transaction Platform</h1><span>Secure account & transfer workspace</span></div><button *ngIf="token()" class="ghost" (click)="logout()">Sign out</button></header>
  <section *ngIf="!token()" class="login card"><h2>Sign in</h2><p>JWT-secured banking operations</p><input [(ngModel)]="username" placeholder="Username"><input [(ngModel)]="password" type="password" placeholder="Password"><button (click)="login()">Sign in</button><small *ngIf="error()" class="error">{{error()}}</small></section>
  <main *ngIf="token()" class="grid">
   <section class="metrics"><article class="metric"><span>Accounts</span><strong>{{accounts().length}}</strong></article><article class="metric"><span>Available balance</span><strong>{{totalBalance() | number:'1.2-2'}}</strong></article><article class="metric"><span>Environment</span><strong>Local</strong></article></section>
   <section class="panel"><div class="panel-head"><div><h2>Accounts</h2><span>Authorized accounts for the current user</span></div><button class="ghost" (click)="loadAccounts()">Refresh</button></div><div class="account-list"><article *ngFor="let a of accounts()" class="account"><div><strong>{{a.accountNumber}}</strong><span>{{a.accountHolderName}}</span></div><div class="balance">{{a.balance | number:'1.2-2'}} <small>{{currency}}</small></div><span class="badge">{{a.status}}</span></article><p *ngIf="!accounts().length">No accounts available.</p></div></section>
   <section class="panel"><div class="panel-head"><div><h2>Transfer money</h2><span>Idempotent API with transactional outbox</span></div></div><div class="form"><label>Source account<select [(ngModel)]="source"><option *ngFor="let a of accounts()" [value]="a.accountNumber">{{a.accountNumber}}</option></select></label><label>Destination account<select [(ngModel)]="destination"><option *ngFor="let a of accounts()" [value]="a.accountNumber">{{a.accountNumber}}</option></select></label><label>Amount<input [(ngModel)]="amount" type="number" min="0.01" step="0.01"></label><label>Currency<input [(ngModel)]="currency" maxlength="3"></label><button (click)="transfer()">Submit transfer</button></div><small *ngIf="message()" [class.error]="messageType==='error'">{{message()}}</small></section>
  </main>
 </div>`,
 styles:[`
 :host{font-family:Inter,system-ui,sans-serif;color:#172033}.app{min-height:100vh;background:#f5f7fb}.topbar{padding:28px 6vw;background:#fff;border-bottom:1px solid #e6eaf0;display:flex;justify-content:space-between;align-items:center}.topbar h1{margin:4px 0;font-size:28px}.topbar span{color:#718096}.eyebrow{font-size:11px;letter-spacing:1.6px;color:#637083}.grid{max-width:1180px;margin:0 auto;padding:30px 24px}.metrics{display:grid;grid-template-columns:repeat(3,1fr);gap:16px}.metric,.panel,.card{background:#fff;border:1px solid #e4e8ef;border-radius:16px;box-shadow:0 8px 24px rgba(23,32,51,.05)}.metric{padding:22px}.metric span,.account span{display:block;color:#718096;font-size:13px}.metric strong{display:block;font-size:26px;margin-top:8px}.panel{margin-top:18px;padding:24px}.panel-head{display:flex;justify-content:space-between;align-items:center}.panel-head h2{margin:0 0 4px}.panel-head span{color:#718096;font-size:13px}.account-list{margin-top:18px}.account{display:grid;grid-template-columns:1fr auto auto;gap:20px;align-items:center;padding:16px 0;border-top:1px solid #edf0f4}.balance{font-weight:700}.badge{padding:5px 9px;border-radius:999px;background:#eef7f0;color:#26733a}.form{display:grid;grid-template-columns:repeat(2,1fr);gap:14px;margin-top:18px}.form label{display:flex;flex-direction:column;gap:6px;font-size:13px;color:#536174}.form input,.form select,.login input{padding:11px;border:1px solid #d7dde7;border-radius:9px;font:inherit}.form button,.login button{grid-column:span 2;padding:12px;border:0;border-radius:9px;background:#172033;color:white;font-weight:700}.ghost{background:white;border:1px solid #d7dde7;padding:9px 13px;border-radius:9px}.login{max-width:420px;margin:12vh auto;padding:30px;display:flex;flex-direction:column;gap:12px}.login button{grid-column:auto}.error{color:#b42318}@media(max-width:760px){.metrics,.form{grid-template-columns:1fr}.form button{grid-column:auto}.account{grid-template-columns:1fr}.topbar{align-items:flex-start;gap:12px}}`
 ]
})
export class AppComponent {
 private http=inject(HttpClient); token=signal<string|null>(localStorage.getItem('accessToken')); accounts=signal<Account[]>([]); error=signal(''); message=signal(''); messageType='success'; username='demo'; password='password'; source=''; destination=''; amount=100; currency='USD';
 totalBalance(){return this.accounts().reduce((sum,a)=>sum+Number(a.balance),0);}
 login(){this.error.set('');this.http.post<LoginResponse>('/api/v1/auth/login',{username:this.username,password:this.password}).subscribe({next:r=>{localStorage.setItem('accessToken',r.accessToken);this.token.set(r.accessToken);this.loadAccounts();},error:()=>this.error.set('Invalid credentials or API unavailable.')});}
 headers(){return new HttpHeaders({Authorization:`Bearer ${this.token()}`});}
 loadAccounts(){this.http.get<Account[]>('/api/v1/accounts',{headers:this.headers()}).subscribe({next:r=>{this.accounts.set(r);this.source ||= r[0]?.accountNumber || '';this.destination ||= r[1]?.accountNumber || '';},error:()=>this.error.set('Unable to load accounts.')});}
 transfer(){const key=crypto.randomUUID();this.http.post('/api/v1/transfers',{sourceAccount:this.source,destinationAccount:this.destination,amount:Number(this.amount),currency:this.currency.toUpperCase()},{headers:this.headers().set('Idempotency-Key',key)}).subscribe({next:()=>{this.messageType='success';this.message.set('Transfer completed successfully.');this.loadAccounts();},error:(e)=>{this.messageType='error';this.message.set(e?.error?.message||'Transfer failed.');}});}
 logout(){localStorage.removeItem('accessToken');this.token.set(null);this.accounts.set([]);}
}
