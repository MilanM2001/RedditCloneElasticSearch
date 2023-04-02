import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunityItemComponent } from './community-item.component';

describe('CommunityItemComponent', () => {
  let component: CommunityItemComponent;
  let fixture: ComponentFixture<CommunityItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommunityItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunityItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
