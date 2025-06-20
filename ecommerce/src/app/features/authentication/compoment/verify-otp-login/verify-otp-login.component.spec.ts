import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyOtpLoginComponent } from './verify-otp-login.component';

describe('VerifyOtpLoginComponent', () => {
  let component: VerifyOtpLoginComponent;
  let fixture: ComponentFixture<VerifyOtpLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerifyOtpLoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyOtpLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
