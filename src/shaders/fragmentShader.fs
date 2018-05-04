#version 330

in vec2 passTextures;

out vec4 fragColour;

uniform sampler2D modelTexture;

void main(void) {
	
	fragColour = texture(modelTexture, passTextures);
	
	if(fragColour.a < 0.1)
        discard;
}