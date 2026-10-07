import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  template: `
    <main class="shell">
      <header>
        <div>
          <h1>Banking Transaction Platform</h1>
          <p>Enterprise transaction and custody operations</p>
        </div>
        <span class="status">API ready</span>
      </header>

      <section class="cards">
        <article><strong>Accounts</strong><span>Customer account operations</span></article>
        <article><strong>Transfers</strong><span>Idempotent money movement</span></article>
        <article><strong>Audit</strong><span>Traceable transaction lifecycle</span></article>
      </section>
    </main>
  `,
  styles: [`
    :host { font-family: Inter, system-ui, sans-serif; display:block; }
    .shell { max-width:1200px; margin:0 auto; padding:48px 24px; }
    header { display:flex; justify-content:space-between; align-items:center; gap:24px; }
    h1 { margin:0; font-size:32px; }
    p { color:#64748b; }
    .status { border:1px solid #cbd5e1; border-radius:999px; padding:8px 14px; }
    .cards { display:grid; grid-template-columns:repeat(3,1fr); gap:20px; margin-top:40px; }
    article { border:1px solid #e2e8f0; border-radius:16px; padding:24px; display:flex; flex-direction:column; gap:8px; }
    article span { color:#64748b; }
    @media(max-width:700px) { .cards { grid-template-columns:1fr; } header { align-items:flex-start; flex-direction:column; } }
  `]
})
export class AppComponent {}
