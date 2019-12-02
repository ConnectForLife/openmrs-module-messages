export interface RouteQueryParams<Params extends { [K in keyof Params]?: string } = {}> {
    queryParams: Params
}
