import { test, expect } from '@playwright/test';
test('customer can submit a transfer', async ({page})=>{
 await page.goto('/');
 await page.getByPlaceholder('Username').fill('demo');
 await page.getByPlaceholder('Password').fill('password');
 await page.getByRole('button',{name:'Sign in'}).click();
 await page.getByLabel('Source account').selectOption('ACC100001');
 await page.getByLabel('Destination account').selectOption('ACC100002');
 await page.getByLabel('Amount').fill('10');
 await page.getByLabel('Currency').fill('USD');
 await page.getByRole('button',{name:'Submit transfer'}).click();
 await expect(page.getByText('Transfer completed successfully.')).toBeVisible();
});
