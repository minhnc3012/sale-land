import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILandPhoto, getLandPhotoIdentifier } from '../land-photo.model';

export type EntityResponseType = HttpResponse<ILandPhoto>;
export type EntityArrayResponseType = HttpResponse<ILandPhoto[]>;

@Injectable({ providedIn: 'root' })
export class LandPhotoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/land-photos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(landPhoto: ILandPhoto): Observable<EntityResponseType> {
    return this.http.post<ILandPhoto>(this.resourceUrl, landPhoto, { observe: 'response' });
  }

  update(landPhoto: ILandPhoto): Observable<EntityResponseType> {
    return this.http.put<ILandPhoto>(`${this.resourceUrl}/${getLandPhotoIdentifier(landPhoto) as number}`, landPhoto, {
      observe: 'response',
    });
  }

  partialUpdate(landPhoto: ILandPhoto): Observable<EntityResponseType> {
    return this.http.patch<ILandPhoto>(`${this.resourceUrl}/${getLandPhotoIdentifier(landPhoto) as number}`, landPhoto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILandPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILandPhoto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLandPhotoToCollectionIfMissing(
    landPhotoCollection: ILandPhoto[],
    ...landPhotosToCheck: (ILandPhoto | null | undefined)[]
  ): ILandPhoto[] {
    const landPhotos: ILandPhoto[] = landPhotosToCheck.filter(isPresent);
    if (landPhotos.length > 0) {
      const landPhotoCollectionIdentifiers = landPhotoCollection.map(landPhotoItem => getLandPhotoIdentifier(landPhotoItem)!);
      const landPhotosToAdd = landPhotos.filter(landPhotoItem => {
        const landPhotoIdentifier = getLandPhotoIdentifier(landPhotoItem);
        if (landPhotoIdentifier == null || landPhotoCollectionIdentifiers.includes(landPhotoIdentifier)) {
          return false;
        }
        landPhotoCollectionIdentifiers.push(landPhotoIdentifier);
        return true;
      });
      return [...landPhotosToAdd, ...landPhotoCollection];
    }
    return landPhotoCollection;
  }
}
