import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnedStockContainer } from './owned-stock-container';

describe('OwnedStockContainer', () => {
  let component: OwnedStockContainer;
  let fixture: ComponentFixture<OwnedStockContainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnedStockContainer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OwnedStockContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
