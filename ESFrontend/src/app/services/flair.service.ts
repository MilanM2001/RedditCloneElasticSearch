import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Flair } from '../model/flair.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FlairService {
    private url = "flairs";
    constructor(private http: HttpClient) {}

    public GetAll(): Observable<Flair[]> {
        return this.http.get<Flair[]>(`${environment.baseApiUrl}/${this.url}/all`);
    }
}