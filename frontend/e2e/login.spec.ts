import { test, expect } from '@playwright/test';
test('customer can sign in and view authorized accounts', async ({page})=>{
 await page.goto('/');
 await expect(page.getByRole('heading',{name:'Sign in'})).toBeVisible();
 await page.getByPlaceholder('Username').fill('demo');
 await page.getByPlaceholder('Password').fill('password');
 await page.getByRole('button',{name:'Sign in'}).click();
 await expect(page.getByRole('heading',{name:'Accounts'})).toBeVisible();
 await expect(page.getByText('ACC100001')).toBeVisible();
});
