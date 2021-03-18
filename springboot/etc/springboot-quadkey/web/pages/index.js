import React, { useEffect, useState } from 'react';
import { toQuaKey } from "quadkey";
import GoogleMapReact from "google-map-react";
import Marker from "../components/Marker";
import { apiClient } from "../api/api";

function Index(props) {

    const [ latLon, setLatLon ] = useState({ lat: 59.955413, lon: 30.337844 });
    const [ level, setLevel ] = useState(11);
    const [ places, setPlaces ] = useState([]);

    const fetchPlaces = async () => {
        const {lat, lon} = latLon;
        const response = await apiClient
            .get(`/api/v1/places/find?lat=${lat}&lon=${lon}&quadkey=${(toQuaKey(lat, lon, level))}&size=50&page=1&kilometer=100`);
        setPlaces(response.data);
    };

    useEffect(() => {
        fetchPlaces();
    }, []);

    return (
        <div style={{height: '100vh', width: '100%'}}>
            <GoogleMapReact
                bootstrapURLKeys={{key: process.env.NEXT_PUBLIC_GOOGLE_API_KEY}}
                defaultZoom={11}
                defaultCenter={{lat: 59.95, lng: 30.33}}
            >
                {
                    places.map(place => (
                        <Marker
                            key={place.id}
                            lat={place.location.lat}
                            lng={place.location.lon}
                            text={place.name}
                        />
                    ))
                }

            </GoogleMapReact>
        </div>
    );
}

export default Index;