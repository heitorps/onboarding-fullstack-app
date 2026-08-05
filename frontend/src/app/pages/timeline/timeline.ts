import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ReviewService } from '../../services/review';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-timeline',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './timeline.html',
  styleUrl: './timeline.css',
})
export class Timeline implements OnInit{
  reviews: any[] = [];
  errorMessage = '';
  isLoading: boolean = true;

  private reviewService = inject(ReviewService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.loadTimeline();
  }

  loadTimeline(){
    this.reviewService.getTimeline().subscribe({
      next: (data) => {
        console.log('Dados recebidos do Spring:', data);
        this.reviews = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Não foi possível carregar a timeline';
        this.isLoading = false;
        this.cdr.detectChanges();
        console.error(err);
      }
    });
  }
}
