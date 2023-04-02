import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { Rule } from 'src/app/model/rule.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-community-view',
  templateUrl: './community-view.component.html',
  styleUrls: ['./community-view.component.css']
})
export class CommunityViewComponent implements OnInit {

  @Input() community: Community = new Community();
  posts: Post[] = [];
  rules: Rule[] = [];
  // flairs: Flair[] = [];

  community_id: number = Number(this.route.snapshot.paramMap.get('community_id'));

  constructor(private communityService: CommunityService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    this.communityService.GetSingle(this.community_id)
      .subscribe({
        next: (data: Community) => {
          this.community = data as Community;
        },
        error: (error) => {
          console.log(error);
        }
      });

    this.communityService.GetCommunityPosts(this.community_id)
      .subscribe({
        next: (data: Post[]) => {
          this.posts = data as Post[];
        },
        error: (error) => {
          console.log(error);
        }
      });

    this.communityService.GetCommunityRules(this.community_id)
      .subscribe({
        next: (data: Rule[]) => {
          this.rules = data as Rule[];
          console.log(this.rules);
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  public isLoggedIn(): boolean {
    if (localStorage.getItem("authToken") != null) {
      return true;
    }
    else {
      return false;
    }
  }

  notLoggedIn(): boolean {
    if (localStorage.getItem("authToken") === null) {
      return true
    }
    else {
      return false
    }
  }

  navigateTo(value: string){
    this.router.navigate([value, this.community.community_id]);
  }

}
