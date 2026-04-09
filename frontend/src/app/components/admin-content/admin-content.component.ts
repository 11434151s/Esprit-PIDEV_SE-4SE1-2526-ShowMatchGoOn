import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
} from 'lucide-angular';

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
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './admin-content.component.html',
  styleUrls: ['./admin-content.component.css'],
})
export class AdminContentComponent {
  readonly SearchIcon = Search;
  readonly PlusIcon = Plus;
  readonly Edit2Icon = Edit2;
  readonly Trash2Icon = Trash2;
  readonly StarIcon = Star;
  readonly EyeIcon = Eye;
  readonly FilterIcon = Filter;
  readonly DownloadIcon = Download;

  searchQuery = signal('');
  filterPlatform = signal('all');
  filterStatus = signal('all');

  content: Content[] = [
    {
      id: '1',
      title: 'Cosmic Odyssey',
      platform: 'Netflix',
      genre: 'Sci-Fi',
      rating: 4.8,
      views: 245000,
      status: 'active',
      hiddenGem: true,
      releaseDate: '2026-01-15',
    },
    {
      id: '2',
      title: 'The Last Kingdom',
      platform: 'Prime Video',
      genre: 'Drama',
      rating: 4.6,
      views: 189000,
      status: 'active',
      hiddenGem: false,
      releaseDate: '2026-01-20',
    },
    {
      id: '3',
      title: 'Midnight Tales',
      platform: 'Disney+',
      genre: 'Horror',
      rating: 4.3,
      views: 156000,
      status: 'hidden',
      hiddenGem: true,
      releaseDate: '2026-01-10',
    },
    {
      id: '4',
      title: 'Summer Dreams',
      platform: 'Hulu',
      genre: 'Romance',
      rating: 4.5,
      views: 201000,
      status: 'active',
      hiddenGem: false,
      releaseDate: '2026-02-01',
    },
    {
      id: '5',
      title: 'Dark Waters',
      platform: 'HBO Max',
      genre: 'Thriller',
      rating: 4.7,
      views: 178000,
      status: 'scheduled',
      hiddenGem: true,
      releaseDate: '2026-02-15',
    },
  ];

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
}
