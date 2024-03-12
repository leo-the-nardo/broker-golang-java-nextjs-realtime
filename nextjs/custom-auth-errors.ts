import { AuthError } from "next-auth";

export class WrongProviderError extends AuthError {
  static type = `WrongProvider`;
  constructor() {
    super(WrongProviderError.type);
  }
}

export class EmailVerification extends AuthError {
  static type = `EmailVerification`;
  constructor() {
    super(EmailVerification.type);
  }
}

export class TwoFactor extends AuthError {
  static type = `TwoFactor`;
  constructor() {
    super(TwoFactor.type);
  }
}
