import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

/**
 * Guard qui vérifie si l'utilisateur est connecté.
 * Si non, il le redirige vers /login avec le paramètre ?redirect=
 * pour qu'après le login, il soit renvoyé automatiquement vers la WatchParty.
 *
 * ⚠️  Adapte la logique isLoggedIn() à ton propre AuthService.
 */
export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const router = inject(Router);

  const isLoggedIn = checkAuth();

  if (isLoggedIn) {
    return true;
  }

  // Sauvegarde l'URL cible pour rediriger après le login
  router.navigate(['/login'], {
    queryParams: { redirect: state.url }
  });

  return false;
};

/**
 * Vérifie la présence d'un token valide.
 * Adapte cette fonction à ton AuthService (ex: inject(AuthService).isLoggedIn())
 */
function checkAuth(): boolean {
  try {
    const token = localStorage.getItem('token');
    if (!token) return false;

    // Vérifier l'expiration du JWT
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp;

    if (exp && Date.now() / 1000 > exp) {
      localStorage.removeItem('token');
      return false;
    }

    return true;
  } catch {
    return false;
  }
}