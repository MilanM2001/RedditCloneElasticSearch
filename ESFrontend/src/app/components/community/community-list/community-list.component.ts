import { Component, Input, OnInit } from '@angular/core';
import { Community } from 'src/app/model/community.model';

@Component({
  selector: 'app-community-list',
  templateUrl: './community-list.component.html',
  styleUrls: ['./community-list.component.css']
})
export class CommunityListComponent implements OnInit {

  @Input() communities: Community[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
