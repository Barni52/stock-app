import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockContainer } from './stock-container';

describe('StockContainer', () => {
  let component: StockContainer;
  let fixture: ComponentFixture<StockContainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockContainer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StockContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
