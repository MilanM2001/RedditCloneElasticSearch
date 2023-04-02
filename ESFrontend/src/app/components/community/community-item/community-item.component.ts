import { Component, Input, OnInit } from '@angular/core';
import { Community } from 'src/app/model/community.model';

@Component({
  selector: 'app-community-item',
  templateUrl: './community-item.component.html',
  styleUrls: ['./community-item.component.css']
})
export class CommunityItemComponent implements OnInit {
  
  @Input() community: Community = new Community();

  constructor() { }

  ngOnInit() {
  }

}
