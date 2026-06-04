import { Injectable, signal } from '@angular/core';

const THEME_KEY = 'est-admin-theme';

@Injectable({ providedIn: 'root' })
export class AdminUiService {
  readonly theme = signal<'dark' | 'light'>('dark');

  constructor() {
    this.loadTheme();
  }

  loadTheme(): void {
    try {
      const stored = localStorage.getItem(THEME_KEY);
      this.applyTheme(stored === 'light' ? 'light' : 'dark');
    } catch {
      this.applyTheme('dark');
    }
  }

  toggleTheme(): void {
    this.applyTheme(this.theme() === 'light' ? 'dark' : 'light');
  }

  applyTheme(theme: 'dark' | 'light'): void {
    this.theme.set(theme);
    if (theme === 'light') {
      document.body.setAttribute('data-theme', 'light');
    } else {
      document.body.removeAttribute('data-theme');
    }
    try {
      localStorage.setItem(THEME_KEY, theme);
    } catch {
      /* ignore */
    }
  }

  toast(message: string, type: 'info' | 'success' | 'error' = 'info'): void {
    let host = document.getElementById('ui-toast-host');
    if (!host) {
      host = document.createElement('div');
      host.id = 'ui-toast-host';
      host.className = 'ui-toast-host';
      host.setAttribute('aria-live', 'polite');
      document.body.appendChild(host);
    }
    const el = document.createElement('div');
    el.className = `ui-toast ui-toast--${type}`;
    el.textContent = message;
    host.appendChild(el);
    requestAnimationFrame(() => el.classList.add('is-visible'));
    setTimeout(() => {
      el.classList.remove('is-visible');
      setTimeout(() => el.remove(), 280);
    }, 2800);
  }
}
