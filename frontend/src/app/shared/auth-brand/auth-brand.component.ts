import { Component, input } from '@angular/core';

@Component({
  selector: 'app-auth-brand',
  template: `
    <a class="dash-brand auth-gate-brand">
      <div class="dash-brand-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M2 18c2.5-3 5.5-4.5 10-4.5S19.5 15 22 18H2zm8-14c-1.5 2-2.5 4-2.5 6a2.5 2.5 0 0 0 5 0c0-2-1-4-2.5-6z" />
        </svg>
      </div>
      <div>
        <span class="dash-brand-text">EST Natation</span>
        <span class="dash-brand-sub">{{ subtitle() }}</span>
      </div>
    </a>
  `,
})
export class AuthBrandComponent {
  readonly subtitle = input('Administration du club');
}
