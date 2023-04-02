import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunityAddComponent } from './community-add.component';

describe('CommunityAddComponent', () => {
  let component: CommunityAddComponent;
  let fixture: ComponentFixture<CommunityAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommunityAddComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunityAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
