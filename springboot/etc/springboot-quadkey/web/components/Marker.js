import React from 'react';
import styled from "@emotion/styled";

const MarkerStyle = styled.div`
    //background: lightseagreen;
    //width: 2.5rem;
    //height: 2.5rem;
    //border-radius: 5px;
    //border: 2px solid #333;
`;

const Image = styled.img`
    width: 3rem;
    height: 3rem;
    border-radius: 100%;
    border: 1px solid #ddd;
`

const Icon = styled.div`
    width: 0.5rem;
    height: 0.5rem;
    border-radius: 100%;
    background: palevioletred;
`

function Marker({ place }) {
    return (
        <MarkerStyle>
            {place.thumbnail_url ? <Image src={place.thumbnail_url}/> : <Icon />}
        </MarkerStyle>
    );
}

export default Marker;