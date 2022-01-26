import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Observable, of } from 'rxjs';
import { catchError, map, takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { HttpClient } from '@angular/common/http';
import { MapInfoWindow, MapMarker } from '@angular/google-maps';
import { GOOGLE_API_KEY, GOOGLE_API_URL } from '../config/googleapis.constants';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild(MapInfoWindow) infoWindow?: MapInfoWindow;

  title = 'resize-observer';
  width = 0;
  height = 0;
  domRectReadOnly: DOMRectReadOnly | undefined;

  apiLoaded: Observable<boolean>;
  center = { lat: 24, lng: 12 };
  zoom = 4;
  display: any;
  markerOptions: google.maps.MarkerOptions = { draggable: true };
  markerPositions: google.maps.LatLngLiteral[] = [];
  vertices: google.maps.LatLngLiteral[] = [
    { lat: 13, lng: 13 },
    { lat: -13, lng: 0 },
    { lat: 13, lng: -13 },
  ];

  imageUrl = 'https://angular.io/assets/images/logos/angular/angular.svg';
  imageBounds: google.maps.LatLngBoundsLiteral = {
    east: 10,
    north: 10,
    south: -10,
    west: -10,
  };

  circleCenter: google.maps.LatLngLiteral = { lat: 10, lng: 15 };
  radius = 3;

  kmlUrl = 'https://developers.google.com/maps/documentation/javascript/examples/kml/westcampus.kml';

  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private httpClient: HttpClient) {
    this.apiLoaded = httpClient.jsonp(GOOGLE_API_URL + '?key=' + GOOGLE_API_KEY, 'callback').pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  moveMap(event: google.maps.MapMouseEvent): void {
    if (event.latLng) {
      this.center = event.latLng.toJSON();
    }
  }

  move(event: google.maps.MapMouseEvent): void {
    if (event.latLng) {
      this.display = event.latLng.toJSON();
    }
  }

  addMarker(event: google.maps.MapMouseEvent): void {
    if (event.latLng) {
      this.markerPositions.push(event.latLng.toJSON());
    }
  }

  openInfoWindow(marker: MapMarker): void {
    this.infoWindow?.open(marker);
  }

  onResize(entry: ResizeObserverEntry[]): void {
    this.width = entry[0].contentRect.width;
    this.height = entry[0].contentRect.height;
    this.domRectReadOnly = entry[0].contentRect;
  }
}
