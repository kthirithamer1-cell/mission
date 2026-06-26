import { Component, input } from '@angular/core';

@Component({
  selector: 'app-auth-brand',
  template: `
    <a class="dash-brand auth-gate-brand">
      <div class="dash-brand-icon" aria-hidden="true">
        <img src="/logo.png" alt="" class="dash-brand-logo" />
      </div>
      <div class="dash-brand-copy">
        <span class="dash-brand-text">Aquapulse</span>
        <span class="dash-brand-sub">{{ subtitle() }}</span>
      </div>
    </a>
  `,
})
export class AuthBrandComponent {
  readonly subtitle = input('Swimming Club Management');
}
