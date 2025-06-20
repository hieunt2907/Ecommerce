import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyOtpRegisterComponent } from './verify-otp-register.component';

describe('VerifyOtpRegisterComponent', () => {
  let component: VerifyOtpRegisterComponent;
  let fixture: ComponentFixture<VerifyOtpRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerifyOtpRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyOtpRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
