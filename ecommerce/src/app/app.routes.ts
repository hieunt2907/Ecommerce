import { Routes } from '@angular/router';
import {HomeComponent} from './features/home/compoment/home.component';
import {LoginComponent} from './features/authentication/compoment/login/login.component';
import {RegisterComponent} from './features/authentication/compoment/register/register.component';
import {ForgotPasswordComponent} from './features/authentication/compoment/forgot-password/forgot-password.component';
import {ResetPasswordComponent} from './features/authentication/compoment/reset-password/reset-password.component';
import {VerifyOtpResetPasswordComponent} from './features/authentication/compoment/verify-otp-reset-password/verify-otp-reset-password.component';
import {VerifyOtpLoginComponent} from './features/authentication/compoment/verify-otp-login/verify-otp-login.component';
import {VerifyOtpRegisterComponent} from './features/authentication/compoment/verify-otp-register/verify-otp-register.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'verify-otp-login', component: VerifyOtpLoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: 'verify-otp-reset-password', component: VerifyOtpResetPasswordComponent},
  {path: 'verify-otp-register', component: VerifyOtpRegisterComponent},
];

