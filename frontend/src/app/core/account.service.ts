import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Account {
  accountNumber: string;
  accountHolderName: string;
  balance: number;
  status: 'ACTIVE' | 'BLOCKED' | 'CLOSED';
}

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);

  findAll(): Observable<Account[]> {
    return this.http.get<Account[]>('/api/v1/accounts');
  }
}
