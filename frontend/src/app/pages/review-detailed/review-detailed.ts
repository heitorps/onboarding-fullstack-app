import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ReviewService } from '../../services/review';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-review-detailed',
  imports: [CommonModule/*, RouterLink */],
  templateUrl: './review-detailed.html',
  styleUrl: './review-detailed.css',
})
export class ReviewDetailed implements OnInit{
  private route = inject(ActivatedRoute);
  private reviewService = inject(ReviewService);
  private cdr = inject(ChangeDetectorRef);
  private location = inject(Location);

  review: any = null;
  isLoading = true;
  errorMessage = '';

  ngOnInit(){
    const idFromUrl = this.route.snapshot.paramMap.get('reviewId');

    if(idFromUrl){
      this.loadReviewDetails(+idFromUrl);
    }
  }

  goBack(){
    this.location.back();
  }

  loadReviewDetails(reviewId: number){
    console.log('Buscando dados da review id ', reviewId);
    this.reviewService.getReviewById(reviewId).subscribe({
      next: (data) => {
        this.review = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Não foi possível carregar a review detalhada.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
