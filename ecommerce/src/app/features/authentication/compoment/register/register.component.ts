// register.component.ts - Updated version

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  styleUrls: ['./register.component.css'],
  standalone: true
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  isSubmitted = false;
  showPassword = false;
  errorMessage = '';
  successMessage = false;
  passwordStrength = 0;
  passwordStrengthText = '';
  passwordStrengthColor = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator()]],
      phone: ['', [Validators.required]]
    });
  }

  onSubmit() {
    this.isSubmitted = true;
    this.errorMessage = '';

    console.log('AuthService:', this.authService);

    if (this.registerForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;

    // FIX: Validate password before sending
    const password = this.registerForm.value.password;
    if (!password || password.trim() === '') {
      this.isLoading = false;
      this.errorMessage = 'Mật khẩu không được để trống';
      return;
    }

    const registerData = {
      username: this.registerForm.value.username.trim(),
      email: this.registerForm.value.email.trim(),
      passwordHash: password, // Map to passwordHash for backend
      phone: this.registerForm.value.phone ? this.registerForm.value.phone.trim() : ''
    };

    console.log('Sending registration data:', {
      ...registerData,
      passwordHash: '[HIDDEN]' // Don't log actual password
    });

    if (!this.authService) {
      console.error('AuthService is not available');
      this.isLoading = false;
      this.errorMessage = 'Service unavailable. Please try again.';
      return;
    }

    this.authService.requestRegister(registerData).subscribe({
      next: (response) => {
        console.log('Registration response:', response);
        this.isLoading = false;
        this.successMessage = true;

        localStorage.setItem('registerEmail', registerData.email);

        this.router.navigate(['/verify-otp-register'], {
          state: {
            sessionId: response.sessionId,
            email: registerData.email
          },
          replaceUrl: true
        });
      },
      error: (error) => {
        console.error('Registration error:', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error.error?.message) {
      const message = error.error.message;
      if(message.includes('Username already exists')) return 'Tên hiển thị đã được sử dụng'
      if (message.includes('Email already exists')) return 'Email đã được sử dụng';
      if (message.includes('Phone number already exists')) return 'Số điện thoại đã được sử dụng';
      if (message.includes('Password cannot be empty')) return 'Mật khẩu không được để trống';
      return message;
    }
    return 'Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại.';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  markFormGroupTouched() {
    Object.keys(this.registerForm.controls).forEach(key => {
      const control = this.registerForm.get(key);
      control?.markAsTouched();
    });
  }

  getUsernameError(): string {
    const control = this.registerForm.get('username');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập tên hiển thị';
      if (control.errors['minlength']) return 'Tên hiển thị  phải có ít nhất 3 ký tự';
    }
    return '';
  }

  getEmailError(): string {
    const control = this.registerForm.get('email');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập email';
      if (control.errors['email']) return 'Email không hợp lệ';
    }
    return '';
  }

  getPasswordError(): string {
    const control = this.registerForm.get('password');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập mật khẩu';
      if (control.errors['minlength']) return 'Mật khẩu phải có ít nhất 8 ký tự';
      if (control.errors['passwordStrength']) return this.getPasswordRequirementsError();
    }
    return '';
  }

  getPhoneError(): string {
    const control = this.registerForm.get('phone');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập số điện thoại';
    }
    return '';
  }

  passwordValidator() {
    return (control: any) => {
      const value = control.value || '';
      this.updatePasswordStrength(value);
      const hasUppercase = this.checkUppercase(value);
      const hasLowercase = this.checkLowercase(value);
      const hasNumber = this.checkNumber(value);
      const hasSpecialChar = this.checkSpecialChar(value);
      const minLength = value.length >= 8;

      const validConditions = [hasUppercase, hasLowercase, hasNumber, hasSpecialChar].filter(Boolean).length;

      if (!minLength || validConditions < 3) {
        return { passwordStrength: true };
      }
      return null;
    };
  }

  updatePasswordStrength(password: string) {
    if (!password) {
      this.passwordStrength = 0;
      this.passwordStrengthText = '';
      this.passwordStrengthColor = '';
      return;
    }

    const checks = [
      password.length >= 8,
      this.checkUppercase(password),
      this.checkLowercase(password),
      this.checkNumber(password),
      this.checkSpecialChar(password),
      password.length >= 12
    ];
    const strength = checks.filter(check => check).length;

    if (strength <= 2) {
      this.passwordStrength = 1;
      this.passwordStrengthText = 'Yếu';
      this.passwordStrengthColor = '#ef4444';
    } else if (strength <= 4) {
      this.passwordStrength = 2;
      this.passwordStrengthText = 'Trung bình';
      this.passwordStrengthColor = '#f59e0b';
    } else {
      this.passwordStrength = 3;
      this.passwordStrengthText = 'Mạnh';
      this.passwordStrengthColor = '#10b981';
    }
  }

  getPasswordRequirementsError(): string {
    const value = this.registerForm.get('password')?.value || '';
    const requirements = [];
    if (value.length < 8) requirements.push('ít nhất 8 ký tự');
    if (!this.checkUppercase(value)) requirements.push('chữ cái in hoa');
    if (!this.checkLowercase(value)) requirements.push('chữ cái thường');
    if (!this.checkNumber(value)) requirements.push('số');
    if (!this.checkSpecialChar(value)) requirements.push('ký tự đặc biệt');
    return `Mật khẩu phải chứa ${requirements.join(', ')}.`;
  }

  checkUppercase(password: string | null): boolean {
    return /[A-Z]/.test(password || '');
  }

  checkLowercase(password: string | null): boolean {
    return /[a-z]/.test(password || '');
  }

  checkNumber(password: string | null): boolean {
    return /\d/.test(password || '');
  }

  checkSpecialChar(password: string | null): boolean {
    return /[!@#$%^&*(),.?":{}|<>]/.test(password || '');
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
