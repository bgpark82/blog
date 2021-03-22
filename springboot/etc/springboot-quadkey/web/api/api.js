import axios from "axios";

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_SERVER_URL,
    timeout: 100000,
});

export const fetchPlaces = async (quadkey) => {
    const response = await apiClient
        .get(`/api/v1/places/find?lat=0&lon=0&quadkey=${quadkey}&size=50&page=1&kilometer=100`);
    return response.data;
};