#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;
uniform int EndPortalLayers;

in vec4 vertexColor;
in vec4 texProj0;
in vec3 view;

const vec3[] COLORS = vec3[](
    vec3(0.9, 0.9, 0.9),
    vec3(0.65, 0.65, 0.65),
    vec3(0.15, 0.15, 0.15)
);

vec2 angle2vec2(float radians) {
    return vec2(cos(radians), sin(radians));
}

vec3 base_layer() {
    float scale = 0.2;
    mat3 translate = mat3(
        scale, 0.0, 0.0,
        0.0, scale, 0.0,
        0.0, 0.0, scale
    );
    return -view.xyz * translate;
}

vec3 next_layer(float layer) {
    float scale = 0.2 - (layer / 40);
    mat3 translate = mat3(
        scale, 0.0, 0.0,
        0.0, scale, 0.0,
        0.0, 0.0, scale
    );
    return -view.xyz * translate;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, base_layer()).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, next_layer(float(i + 1))).rgb * COLORS[i + 1];
    }
    fragColor = vec4(color, 1.0) * vertexColor * ColorModulator;
}
