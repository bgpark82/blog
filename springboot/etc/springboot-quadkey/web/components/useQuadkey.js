import React, { useCallback, useState } from 'react';
import { getQuadkeys } from "../utils/quadkey";
import { fetchMultiPlaces, fetchPlaces } from "../api/api";

function useQuadkey() {
    const [ places, setPlaces ] = useState([]);

    const onBoundsChange = useCallback(({ center, zoom, bounds, ...other }) => {
        if(!window.map && !window.maps) return;
        if(zoom < 8) return;

        fetchMultiPlaces(getQuadkeys(bounds, zoom))
            .then(place => setPlaces(place.flat()));
        
        // Promise.all(getQuadkeys(bounds, zoom)
        //     .map(quadkey => fetchPlaces(quadkey)))
        //     .then(place => setPlaces(place.flat()));
    },[]);

    const onLoadGoogleMap = useCallback((map, maps) => {
        window.map = map;
        window.maps = maps;
    },[]);

    return [places, onBoundsChange, onLoadGoogleMap]
}

export default useQuadkey;