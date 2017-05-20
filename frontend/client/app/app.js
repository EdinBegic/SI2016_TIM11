import angular from 'angular';
import uiRouter from 'angular-ui-router';
import Components from './components/components';
import AppComponent from './app.component';
import services from './services/services';
import env from './env';
import ShowAuthenticated from './directives/showAuthenticated/showAuthenticated.directive';
import ShowForRole from './directives/showForRole/showForRole.directive';
import authInterceptor  from './interceptors/auth.interceptor';

angular.module('app', [
    uiRouter,
    Components,
    services
])
.constant('ENV', env)
.config(($locationProvider, $httpProvider) => {
    'ngInject';
    // @see: https://github.com/angular-ui/ui-router/wiki/Frequently-Asked-Questions
    // #how-to-configure-your-server-to-work-with-html5mode
    $locationProvider.html5Mode(true).hashPrefix('!');
    $httpProvider.interceptors.push(authInterceptor);
})
.component('app', AppComponent)
.directive('showAuthenticated', ShowAuthenticated)
.directive('showForRole', ShowForRole)
.run((sessionService, $state, $transitions) => {
    'ngInject';
    $transitions.onStart({ to: '*' }, (trans) => {
        if (!sessionService.isUserLoggedIn()) {
            return $state.target('login');
        }
        else {
            return true;
        }
    });
    $transitions.onStart({ to: 'login' }, (trans) => {
        if (sessionService.isUserLoggedIn()) {
            return $state.target('home');
        }
        else {
            return true;
        }
    });

});
