import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderContainer } from './order-container';

describe('OrderContainer', () => {
  let component: OrderContainer;
  let fixture: ComponentFixture<OrderContainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderContainer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
