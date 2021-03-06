import axios from "axios";

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_SERVER_URL,
    // timeout: 2000,
});

export const fetchPlaces = async (quadkey) => {
    console.log(quadkey)
    const response = await apiClient
        .get(`/api/v2/places/find?lat=0&lon=0&quadkey=${quadkey}&size=5&page=1&kilometer=0`);

    return response.data;
};

export const fetchMultiPlaces = async (quadkeys) => {
    const response = await apiClient
        .post(`/api/v3/places/find`, quadkeys)

    return response.data;
};