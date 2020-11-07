import React from 'react'
import Styled from 'styled-components/native'

const Container = Styled.View`
    
`
const InputField = Styled.TextInput`
    
`

const Input = ({
    placeholder, keyboardType, secureTextEntry, style, clearMode, onChangeText
}) => {
    return (
        <Container>
            <InputField
                selectionColor="#FFFFFF"
                secureTextEntry={secureTextEntry}
                keyboardType={keyboardType ? keyboardType : 'default'}
                autoCapitalize="none"
                autoCorrect={false}
                allowFontScaling={false}
                placeholderTextColor="#FFFFFF"
                placeholder={placeholder}
                clearButtonMode={clearMode ? 'while-editing' : 'never'}
                onChangeText={onChangeText}
            />
        </Container>
    )
}

export default Input
