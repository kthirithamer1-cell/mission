import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-photo-upload',
  imports: [CommonModule],
  template: `
    <div class="avatar-container" [style.--accent-color]="accentColor" (click)="fileInput.click()">
      @if (uploading()) {
        <div class="upload-spinner">
          <div class="spinner-icon"></div>
        </div>
      }

      @if (photoUrl) {
        <img [src]="photoUrl" alt="Profile" class="avatar-image" />
      } @else {
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="avatar-fallback">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
          <circle cx="12" cy="7" r="4"></circle>
        </svg>
      }

      <div class="avatar-overlay">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="camera-icon">
          <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
          <circle cx="12" cy="13" r="4"></circle>
        </svg>
      </div>

      <input type="file" #fileInput style="display: none" accept="image/*" (change)="onFileSelected($event)" />
    </div>
  `,
  styles: [`
    .avatar-container {
      position: relative;
      width: 120px;
      height: 120px;
      border-radius: 50%;
      overflow: hidden;
      cursor: pointer;
      border: 4px solid var(--accent-color, #00a693);
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f3f4f6;
      margin: 0 auto;
      transition: transform 0.2s ease;
    }

    .avatar-container:hover {
      transform: scale(1.02);
    }

    .avatar-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .avatar-fallback {
      width: 50%;
      height: 50%;
      color: #9ca3af;
    }

    .avatar-overlay {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.4);
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: opacity 0.2s ease-in-out;
    }

    .avatar-container:hover .avatar-overlay {
      opacity: 1;
    }

    .camera-icon {
      width: 28px;
      height: 28px;
      color: #ffffff;
    }

    .upload-spinner {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(255, 255, 255, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      z-index: 10;
    }

    .spinner-icon {
      width: 24px;
      height: 24px;
      border: 3px solid #f3f4f6;
      border-top: 3px solid var(--accent-color, #00a693);
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
  `]
})
export class PhotoUploadComponent {
  @Input() photoUrl?: string;
  @Input() accentColor: string = '#00a693';
  @Input() uploading = signal<boolean>(false);

  @Output() photoSelected = new EventEmitter<File>();

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.photoSelected.emit(file);
    }
  }
}
