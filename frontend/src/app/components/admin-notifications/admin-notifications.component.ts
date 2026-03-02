import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LucideAngularModule, Bell, Mail, MessageSquare, Send, Edit2, Trash2, X } from 'lucide-angular';
import { NotificationService } from '../../services/api.service';
import { CustomValidators } from '../../services/validators';

export interface Notification {
  id?: string;
  title: string;
  message: string;
  read: boolean;
}

@Component({
  selector: 'app-admin-notifications',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, LucideAngularModule],
  templateUrl: './admin-notifications.component.html',
  styleUrls: ['./admin-notifications.component.css'],
})
export class AdminNotificationsComponent implements OnInit {
  readonly BellIcon = Bell;
  readonly MailIcon = Mail;
  readonly MessageSquareIcon = MessageSquare;
  readonly SendIcon = Send;
  readonly Edit2Icon = Edit2;
  readonly Trash2Icon = Trash2;
  readonly CloseIcon = X;

  notifications: Notification[] = [];
  loading = false;
  error: string | null = null;
  showForm = false;
  editingId: string | null = null;
  notificationForm!: FormGroup;

  constructor(
    private notificationService: NotificationService,
    private fb: FormBuilder
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.loadNotifications();
  }

  initializeForm() {
    this.notificationForm = this.fb.group({
      title: ['', [Validators.required, CustomValidators.minLength(3), CustomValidators.maxLength(100)]],
      message: ['', [Validators.required, CustomValidators.minLength(10), CustomValidators.maxLength(500)]],
    });
  }

  loadNotifications() {
    this.loading = true;
    this.error = null;
    // Load notifications for a default user ID
    this.notificationService.getNotificationsByUserId('1').subscribe({
      next: (data) => {
        this.notifications = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load notifications: ' + err.message;
        this.loading = false;
        console.error('Error loading notifications:', err);
      },
    });
  }

  openForm() {
    this.editingId = null;
    this.notificationForm.reset();
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
    this.editingId = null;
    this.notificationForm.reset();
  }

  saveNotification() {
    if (!this.notificationForm.valid) {
      Object.keys(this.notificationForm.controls).forEach(key => {
        const control = this.notificationForm.get(key);
        if (control && control.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    const formData = {
      ...this.notificationForm.value,
      read: false,
    };

    this.createNotification(formData);
  }

  createNotification(data: Notification) {
    this.loading = true;
    this.notificationService.createNotification(data).subscribe({
      next: (response) => {
        this.notifications.unshift(response);
        this.closeForm();
        this.loading = false;
        alert('Notification sent successfully!');
      },
      error: (err) => {
        this.error = 'Failed to send notification: ' + err.message;
        this.loading = false;
        console.error('Error sending notification:', err);
      },
    });
  }

  markAsRead(id: string) {
    this.notificationService.markAsRead(id).subscribe({
      next: () => {
        const notif = this.notifications.find(n => n.id === id);
        if (notif) {
          notif.read = true;
        }
      },
      error: (err) => {
        console.error('Error marking as read:', err);
      },
    });
  }

  deleteNotification(id: string) {
    if (confirm('Are you sure you want to delete this notification?')) {
      this.loading = true;
      this.notificationService.deleteNotification(id).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(n => n.id !== id);
          this.loading = false;
          alert('Notification deleted successfully!');
        },
        error: (err) => {
          this.error = 'Failed to delete notification: ' + err.message;
          this.loading = false;
          console.error('Error deleting notification:', err);
        },
      });
    }
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  get sentThisWeek(): number {
    return this.notifications.length;
  }

  getFieldError(fieldName: string): string {
    const control = this.notificationForm.get(fieldName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength']) return `${fieldName} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${fieldName} must not exceed ${errors['maxlength'].requiredLength} characters`;

    return 'Invalid value';
  }
}
