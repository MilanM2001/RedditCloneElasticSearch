import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunitySuspendComponent } from './community-suspend.component';

describe('CommunitySuspendComponent', () => {
  let component: CommunitySuspendComponent;
  let fixture: ComponentFixture<CommunitySuspendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommunitySuspendComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunitySuspendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
