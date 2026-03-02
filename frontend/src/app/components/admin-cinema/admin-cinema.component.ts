import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LucideAngularModule, MapPin, Building, Calendar, Plus, Edit2, Trash2, X } from 'lucide-angular';
import { CategoryService } from '../../services/api.service';
import { CustomValidators } from '../../services/validators';

export interface Category {
  id?: string;
  name: string;
  description: string;
}

@Component({
  selector: 'app-admin-cinema',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, LucideAngularModule],
  templateUrl: './admin-cinema.component.html',
  styleUrls: ['./admin-cinema.component.css'],
})
export class AdminCinemaComponent implements OnInit {
  readonly MapPinIcon = MapPin;
  readonly BuildingIcon = Building;
  readonly CalendarIcon = Calendar;
  readonly PlusIcon = Plus;
  readonly Edit2Icon = Edit2;
  readonly Trash2Icon = Trash2;
  readonly CloseIcon = X;

  categories: Category[] = [];
  loading = false;
  error: string | null = null;
  showForm = false;
  editingId: string | null = null;
  categoryForm!: FormGroup;

  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.loadCategories();
  }

  initializeForm() {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required, CustomValidators.minLength(3), CustomValidators.maxLength(100)]],
      description: ['', [Validators.required, CustomValidators.minLength(10), CustomValidators.maxLength(500)]],
    });
  }

  loadCategories() {
    this.loading = true;
    this.error = null;
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load categories: ' + err.message;
        this.loading = false;
        console.error('Error loading categories:', err);
      },
    });
  }

  openForm() {
    this.editingId = null;
    this.categoryForm.reset();
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
    this.editingId = null;
    this.categoryForm.reset();
  }

  editCategory(category: Category) {
    this.editingId = category.id || null;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description,
    });
    this.showForm = true;
  }

  saveCategory() {
    if (!this.categoryForm.valid) {
      Object.keys(this.categoryForm.controls).forEach(key => {
        const control = this.categoryForm.get(key);
        if (control && control.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    if (this.editingId) {
      this.updateCategory(this.editingId, this.categoryForm.value);
    } else {
      this.createCategory(this.categoryForm.value);
    }
  }

  createCategory(data: Category) {
    this.loading = true;
    this.categoryService.createCategory(data).subscribe({
      next: (response) => {
        this.categories.push(response);
        this.closeForm();
        this.loading = false;
        alert('Category created successfully!');
      },
      error: (err) => {
        this.error = 'Failed to create category: ' + err.message;
        this.loading = false;
        console.error('Error creating category:', err);
      },
    });
  }

  updateCategory(id: string, data: Category) {
    this.loading = true;
    this.categoryService.updateCategory(id, data).subscribe({
      next: (response) => {
        const index = this.categories.findIndex(c => c.id === id);
        if (index > -1) {
          this.categories[index] = response;
        }
        this.closeForm();
        this.loading = false;
        alert('Category updated successfully!');
      },
      error: (err) => {
        this.error = 'Failed to update category: ' + err.message;
        this.loading = false;
        console.error('Error updating category:', err);
      },
    });
  }

  deleteCategory(id: string) {
    if (confirm('Are you sure you want to delete this category?')) {
      this.loading = true;
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.categories = this.categories.filter(c => c.id !== id);
          this.loading = false;
          alert('Category deleted successfully!');
        },
        error: (err) => {
          this.error = 'Failed to delete category: ' + err.message;
          this.loading = false;
          console.error('Error deleting category:', err);
        },
      });
    }
  }

  getFieldError(fieldName: string): string {
    const control = this.categoryForm.get(fieldName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength']) return `${fieldName} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${fieldName} must not exceed ${errors['maxlength'].requiredLength} characters`;

    return 'Invalid value';
  }
}
