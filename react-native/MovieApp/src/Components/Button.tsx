import React from 'react'
import Styled from 'styled-components/native'

const StyleButton = Styled.TouchableOpacity`
    width: 100%;
    height: 40px;
`
const Label = Styled.Text`
    color: #FFFFFF
`
const Button = ({label, style, onPress}) => {
    return (
        <StyleButton style={style} onPress={onPress}>
            <Label>{label}</Label>
        </StyleButton>
    )
}

export default Button
