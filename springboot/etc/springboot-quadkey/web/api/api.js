import axios from "axios";

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_SERVER_URL,
    timeout: 100000,
});

export const fetchPlaces = async (quadkey) => {
    console.log(quadkey)
    const response = await apiClient
        .get(`/api/v2/places/find?lat=0&lon=0&quadkey=${quadkey}&size=10&page=1&kilometer=0`);

    return response.data;
};