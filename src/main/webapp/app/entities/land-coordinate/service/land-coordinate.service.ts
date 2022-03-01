import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILandCoordinate, getLandCoordinateIdentifier } from '../land-coordinate.model';

export type EntityResponseType = HttpResponse<ILandCoordinate>;
export type EntityArrayResponseType = HttpResponse<ILandCoordinate[]>;

@Injectable({ providedIn: 'root' })
export class LandCoordinateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/land-coordinates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(landCoordinate: ILandCoordinate): Observable<EntityResponseType> {
    return this.http.post<ILandCoordinate>(this.resourceUrl, landCoordinate, { observe: 'response' });
  }

  update(landCoordinate: ILandCoordinate): Observable<EntityResponseType> {
    return this.http.put<ILandCoordinate>(`${this.resourceUrl}/${getLandCoordinateIdentifier(landCoordinate) as number}`, landCoordinate, {
      observe: 'response',
    });
  }

  partialUpdate(landCoordinate: ILandCoordinate): Observable<EntityResponseType> {
    return this.http.patch<ILandCoordinate>(
      `${this.resourceUrl}/${getLandCoordinateIdentifier(landCoordinate) as number}`,
      landCoordinate,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILandCoordinate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILandCoordinate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLandCoordinateToCollectionIfMissing(
    landCoordinateCollection: ILandCoordinate[],
    ...landCoordinatesToCheck: (ILandCoordinate | null | undefined)[]
  ): ILandCoordinate[] {
    const landCoordinates: ILandCoordinate[] = landCoordinatesToCheck.filter(isPresent);
    if (landCoordinates.length > 0) {
      const landCoordinateCollectionIdentifiers = landCoordinateCollection.map(
        landCoordinateItem => getLandCoordinateIdentifier(landCoordinateItem)!
      );
      const landCoordinatesToAdd = landCoordinates.filter(landCoordinateItem => {
        const landCoordinateIdentifier = getLandCoordinateIdentifier(landCoordinateItem);
        if (landCoordinateIdentifier == null || landCoordinateCollectionIdentifiers.includes(landCoordinateIdentifier)) {
          return false;
        }
        landCoordinateCollectionIdentifiers.push(landCoordinateIdentifier);
        return true;
      });
      return [...landCoordinatesToAdd, ...landCoordinateCollection];
    }
    return landCoordinateCollection;
  }
}
