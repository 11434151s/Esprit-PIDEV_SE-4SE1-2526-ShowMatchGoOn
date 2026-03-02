import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  LucideAngularModule,
  Search,
  Plus,
  Edit2,
  Trash2,
  Star,
  Eye,
  Filter,
  Download,
  X,
} from 'lucide-angular';
import { ContentService } from '../../services/api.service';
import { CustomValidators } from '../../services/validators';

interface Content {
  id: string;
  title: string;
  platform: string;
  genre: string;
  rating: number;
  views: number;
  status: 'active' | 'hidden' | 'scheduled';
  hiddenGem: boolean;
  releaseDate: string;
}

@Component({
  selector: 'app-admin-content',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, LucideAngularModule],
  templateUrl: './admin-content.component.html',
  styleUrls: ['./admin-content.component.css'],
})
export class AdminContentComponent implements OnInit {
  readonly SearchIcon = Search;
  readonly PlusIcon = Plus;
  readonly Edit2Icon = Edit2;
  readonly Trash2Icon = Trash2;
  readonly StarIcon = Star;
  readonly EyeIcon = Eye;
  readonly FilterIcon = Filter;
  readonly DownloadIcon = Download;
  readonly CloseIcon = X;

  searchQuery = signal('');
  filterPlatform = signal('all');
  filterStatus = signal('all');
  
  contentList = signal<Content[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  showForm = signal(false);
  editingId = signal<string | null>(null);
  contentForm!: FormGroup;

  constructor(
    private contentService: ContentService,
    private fb: FormBuilder
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.loadAllContent();
  }

  initializeForm() {
    this.contentForm = this.fb.group({
      title: ['', [Validators.required, CustomValidators.minLength(3), CustomValidators.maxLength(255)]],
      description: ['', [Validators.required, CustomValidators.minLength(10)]],
      releaseDate: ['', [CustomValidators.dateFormat, CustomValidators.pastDateValidator]],
      duration: ['', [CustomValidators.numeric, CustomValidators.minValue(1)]],
      genre: ['', Validators.required],
      status: ['active', Validators.required],
      hiddenGem: [false],
    });
  }

  loadAllContent() {
    this.loading.set(true);
    this.error.set(null);
    this.contentService.getAllContent().subscribe({
      next: (data) => {
        // Map backend response to Content interface
        const mappedContent = data.map((item: any) => ({
          id: item.id,
          title: item.title,
          platform: item.platform || 'Unknown',
          genre: item.genre || 'Unknown',
          rating: item.rating || 0,
          views: item.views || 0,
          status: item.status || 'active',
          hiddenGem: item.hiddenGem || false,
          releaseDate: item.releaseDate || new Date().toISOString().split('T')[0],
        }));
        this.contentList.set(mappedContent);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load content: ' + err.message);
        this.loading.set(false);
        console.error('Error loading content:', err);
      },
    });
  }

  openForm() {
    this.editingId.set(null);
    this.contentForm.reset({ status: 'active', hiddenGem: false });
    this.showForm.set(true);
  }

  closeForm() {
    this.showForm.set(false);
    this.editingId.set(null);
    this.contentForm.reset();
  }

  editContent(content: Content) {
    this.editingId.set(content.id);
    this.contentForm.patchValue({
      title: content.title,
      description: content.title, // Using title as description placeholder
      releaseDate: content.releaseDate,
      genre: content.genre,
      status: content.status,
      hiddenGem: content.hiddenGem,
    });
    this.showForm.set(true);
  }

  saveContent() {
    if (!this.contentForm.valid) {
      Object.keys(this.contentForm.controls).forEach(key => {
        const control = this.contentForm.get(key);
        if (control && control.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    const id = this.editingId();
    const formData = this.contentForm.value;

    if (id) {
      this.updateContent(id, formData);
    } else {
      this.createContent(formData);
    }
  }

  createContent(data: any) {
    this.loading.set(true);
    this.contentService.createFilm(data).subscribe({
      next: (response) => {
        const newContent: Content = {
          id: response.id || Math.random().toString(),
          title: response.title,
          platform: data.platform || 'Unknown',
          genre: response.genre,
          rating: 0,
          views: 0,
          status: data.status,
          hiddenGem: data.hiddenGem,
          releaseDate: response.releaseDate,
        };
        this.contentList.set([...this.contentList(), newContent]);
        this.closeForm();
        this.loading.set(false);
        alert('Content created successfully!');
      },
      error: (err) => {
        this.error.set('Failed to create content: ' + err.message);
        this.loading.set(false);
        console.error('Error creating content:', err);
      },
    });
  }

  updateContent(id: string, data: any) {
    this.loading.set(true);
    this.contentService.updateFilm(id, data).subscribe({
      next: (response) => {
        const updated = this.contentList().map(item =>
          item.id === id ? {
            ...item,
            ...response,
            status: data.status,
            hiddenGem: data.hiddenGem,
          } : item
        );
        this.contentList.set(updated);
        this.closeForm();
        this.loading.set(false);
        alert('Content updated successfully!');
      },
      error: (err) => {
        this.error.set('Failed to update content: ' + err.message);
        this.loading.set(false);
        console.error('Error updating content:', err);
      },
    });
  }

  deleteContent(id: string) {
    if (confirm('Are you sure you want to delete this content?')) {
      this.loading.set(true);
      this.contentService.deleteContent(id).subscribe({
        next: () => {
          this.contentList.set(this.contentList().filter(item => item.id !== id));
          this.loading.set(false);
          alert('Content deleted successfully!');
        },
        error: (err) => {
          this.error.set('Failed to delete content: ' + err.message);
          this.loading.set(false);
          console.error('Error deleting content:', err);
        },
      });
    }
  }

  get content(): Content[] {
    return this.contentList();
  }

  get filteredContent(): Content[] {
    return this.content.filter((item) => {
      const matchesSearch = item.title.toLowerCase().includes(this.searchQuery().toLowerCase());
      const matchesPlatform = this.filterPlatform() === 'all' || item.platform === this.filterPlatform();
      const matchesStatus = this.filterStatus() === 'all' || item.status === this.filterStatus();
      return matchesSearch && matchesPlatform && matchesStatus;
    });
  }


  get activeCount(): number {
    return this.content.filter((c) => c.status === 'active').length;
  }

  get hiddenGemsCount(): number {
    return this.content.filter((c) => c.hiddenGem).length;
  }

  get averageRating(): number {
    if (this.content.length === 0) return 0;
    const total = this.content.reduce((sum, c) => sum + c.rating, 0);
    return Math.round((total / this.content.length) * 10) / 10;
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'active':
        return 'bg-green-500/20 text-green-500 border-green-500';
      case 'hidden':
        return 'bg-gray-500/20 text-gray-400 border-gray-400';
      case 'scheduled':
        return 'bg-blue-500/20 text-blue-500 border-blue-500';
      default:
        return 'bg-gray-500/20 text-gray-400 border-gray-400';
    }
  }

  getFieldError(fieldName: string): string {
    const control = this.contentForm.get(fieldName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength']) return `${fieldName} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${fieldName} must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['numeric']) return `${fieldName} must be a valid number`;
    if (errors['minvalue']) return `${fieldName} must be at least ${errors['minvalue'].min}`;
    if (errors['dateformat']) return 'Date must be in YYYY-MM-DD format';
    if (errors['pastdate']) return 'Release date cannot be in the past';
    if (errors['futuredate']) return 'Release date cannot be in the future';

    return 'Invalid value';
  }
}

