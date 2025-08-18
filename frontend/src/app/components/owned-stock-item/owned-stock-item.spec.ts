import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnedStockItem } from './owned-stock-item';

describe('OwnedStockItem', () => {
  let component: OwnedStockItem;
  let fixture: ComponentFixture<OwnedStockItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnedStockItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OwnedStockItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
