#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:matrix.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 texProj0;
in vec3 view;

const vec3[] COLORS = vec3[](
    vec3(0.92, 0.92, 0.92),
    vec3(0.75, 0.1, 0.1),
    vec3(0.65, 0.65, 0.1),
    vec3(0.1, 0.75, 0.1),
    vec3(0.1, 0.1, 0.75),
    vec3(0.7, 0.1, 0.6)
);

vec2 angle2vec2(float radians) {
    return vec2(cos(radians), sin(radians));
}

vec3 base_layer() {
    float time = GameTime * 10.0;
    float scale = 0.2;
    mat3 translate = mat3(
        scale, 0.0, 0.0,
        0.0, scale, time,
        0.0, 0.0, scale
    );

    return -view.xyz * translate;
}

vec3 bee_layer(float layer) {
    float time = (GameTime * (25.0 + (layer * 8)));
    float scale = 2.0 - (layer / 100);

    float rotation = ((layer * layer * 4321.0) + layer + 1) * 9.5;
    vec2 dir_vec = angle2vec2(radians(rotation));
    mat3 rotateAndTranslate = mat3(
        dir_vec.x * scale, -dir_vec.y * scale, 0.0,
        dir_vec.y * scale, dir_vec.x * scale, time,
        0.0, 0.0, scale
    );

    return -view.xyz * (layer + 1) * rotateAndTranslate;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, base_layer()).rgb * COLORS[0];
    for (int i = 0; i < LAYERS; i++) {
        color += textureProj(Sampler1, bee_layer(float(i + 1))).rgb * COLORS[i + 1];
    }
    fragColor = apply_fog(vec4(color, 1.0) * vertexColor, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
