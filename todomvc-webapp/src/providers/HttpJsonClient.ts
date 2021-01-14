export class HttpJsonClient {
  constructor(private baseUrl: string) {}

  async execute<I, O>(endpoint: string, request: I): Promise<O> {
    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json;charset=UTF-8',
      },
      body: JSON.stringify(request),
    });
    return response.json();
  }
}
