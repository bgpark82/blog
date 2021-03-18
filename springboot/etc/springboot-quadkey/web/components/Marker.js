import React from 'react';
import styled from "@emotion/styled";

const MarkerStyle = styled.div`
    background: lightseagreen;
    width: 2.5rem;
    height: 2.5rem;
    border-radius: 5px;
    border: 2px solid #333;
`;

function Marker(props) {
    return (
        <MarkerStyle></MarkerStyle>
    );
}

export default Marker;