import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';
import { response } from 'express';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth {
  isLoginMode = true;
  errorMessage = '';
  successMessage = '';

  username = '';
  password = '';

  private authService = inject(AuthService);
  private router = inject(Router);

  toggleMode(){
    this.isLoginMode = !this.isLoginMode;
    this.clearMessages();
  }

  onSubmit(){
    this.clearMessages();

    const payload = {
      username: this.username,
      password: this.password
    };

    if(this.isLoginMode){
      this.authService.login(payload).subscribe({
        next: (response) => {
          localStorage.setItem('userId', response.id);

          this.router.navigate(['/timeline']);
        },
        error: (err) => {
          this.errorMessage = err.error?.error || 'Ocorreu um erro ao tentar fazer login.';
        }
      });
    }else{
      this.authService.register(payload).subscribe({
        next: (response) => {
          this.successMessage = "Conta criada com sucesso! Faça o login para entrar"
          this.isLoginMode = true;
          this.password = '';
        },
        error: (err) => {
          this.errorMessage = err.error?.error || 'Erro ao criar a conta. Tente outro nome';
        }
      });
    }
  }

  private clearMessages(){
    this.errorMessage = '';
    this.successMessage = '';
  }
}
