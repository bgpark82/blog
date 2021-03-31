import React from 'react';
import GoogleMapReact from "google-map-react";
import Marker from "../components/Marker";
import useQuadkey from "../components/useQuadkey";

function Index() {

    const [places, onBoundsChange, onLoadGoogleMap] = useQuadkey();

    return (
        <div style={{height: '100vh', width: '100%'}}>
            <GoogleMapReact
                bootstrapURLKeys={{key: process.env.NEXT_PUBLIC_GOOGLE_API_KEY}}
                defaultCenter={{lat: 59.95, lng: 30.33}}
                defaultZoom={11}
                onChange={onBoundsChange}
                yesIWantToUseGoogleMapApiInternals
                onGoogleApiLoaded={({map, maps}) => onLoadGoogleMap(map, maps)}
            >
                {
                    places.map(place => (
                        <Marker
                            key={place.id}
                            lat={place.location.lat}
                            lng={place.location.lon}
                            text={place.name}
                            place={place}
                        />
                    ))
                }

            </GoogleMapReact>
        </div>
    );
}

export default Index;