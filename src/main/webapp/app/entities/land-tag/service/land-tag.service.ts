import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILandTag, getLandTagIdentifier } from '../land-tag.model';

export type EntityResponseType = HttpResponse<ILandTag>;
export type EntityArrayResponseType = HttpResponse<ILandTag[]>;

@Injectable({ providedIn: 'root' })
export class LandTagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/land-tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(landTag: ILandTag): Observable<EntityResponseType> {
    return this.http.post<ILandTag>(this.resourceUrl, landTag, { observe: 'response' });
  }

  update(landTag: ILandTag): Observable<EntityResponseType> {
    return this.http.put<ILandTag>(`${this.resourceUrl}/${getLandTagIdentifier(landTag) as number}`, landTag, { observe: 'response' });
  }

  partialUpdate(landTag: ILandTag): Observable<EntityResponseType> {
    return this.http.patch<ILandTag>(`${this.resourceUrl}/${getLandTagIdentifier(landTag) as number}`, landTag, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILandTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILandTag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLandTagToCollectionIfMissing(landTagCollection: ILandTag[], ...landTagsToCheck: (ILandTag | null | undefined)[]): ILandTag[] {
    const landTags: ILandTag[] = landTagsToCheck.filter(isPresent);
    if (landTags.length > 0) {
      const landTagCollectionIdentifiers = landTagCollection.map(landTagItem => getLandTagIdentifier(landTagItem)!);
      const landTagsToAdd = landTags.filter(landTagItem => {
        const landTagIdentifier = getLandTagIdentifier(landTagItem);
        if (landTagIdentifier == null || landTagCollectionIdentifiers.includes(landTagIdentifier)) {
          return false;
        }
        landTagCollectionIdentifiers.push(landTagIdentifier);
        return true;
      });
      return [...landTagsToAdd, ...landTagCollection];
    }
    return landTagCollection;
  }
}
