import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommunitiesComponent } from './components/community/communities/communities.component';
import { CommunityAddComponent } from './components/community/community-add/community-add.component';
import { CommunityEditComponent } from './components/community/community-edit/community-edit.component';
import { CommunitySuspendComponent } from './components/community/community-suspend/community-suspend.component';
import { CommunityViewComponent } from './components/community/community-view/community-view.component';
import { LoginComponent } from './components/login/login.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { PostAddComponent } from './components/post/post-add/post-add.component';
import { PostEditComponent } from './components/post/post-edit/post-edit.component';
import { PostViewComponent } from './components/post/post-view/post-view.component';
import { RegisterComponent } from './components/register/register.component';
import { RuleAddComponent } from './components/rule/rule-add/rule-add.component';
import { RuleEditComponent } from './components/rule/rule-edit/rule-edit.component';
import { MyProfileComponent } from './components/user/my-profile/my-profile.component';

const routes: Routes = [
  {
    path: "Login",
    component: LoginComponent
  },
  {
    path: "Register",
    component: RegisterComponent
  },
  {
    path: "Main-Page",
    component: MainPageComponent
  },
  {
    path: 'Communities',
    component: CommunitiesComponent
  },
  {
    path: 'Community-Add',
    component: CommunityAddComponent
  },
  {
    path: 'Community-View/:community_id',
    component: CommunityViewComponent
  },
  {
    path: 'Community-Edit/:community_id',
    component: CommunityEditComponent
  },
  {
    path: 'Community-Suspend/:community_id',
    component: CommunitySuspendComponent
  },
  {
    path: 'Post-View/:post_id',
    component: PostViewComponent
  },
  {
    path: 'Post-Edit/:post_id',
    component: PostEditComponent
  },
  {
    path: 'Post-Add/:community_id',
    component: PostAddComponent
  },
  {
    path: 'Rule-Edit/:rule_id',
    component: RuleEditComponent
  },
  {
    path: 'Rule-Add/:community_id',
    component: RuleAddComponent
  },
  {
    path: 'My-Profile',
    component: MyProfileComponent
  },
  
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
