
import { Output } from 'Paths'
import { join } from 'Path'


const { writeTextFile } = Deno;
const { log } = console;


export default async function generate ( page ){
    
    log(`Generating page '${ page.name }' with ${ page.versions.length } versions.`);
    
    const parts = [];
    
    parts.push(
        `<div align = center>` ,
        `# Changelog <br> Version ${ page.name.charAt(0) }` ,
    )
    

    parts.push('<br>\n<br>')
    
    const buttons = [];
    
    for ( const version of page.versions )
        buttons.push(`[<kbd>  ${ version.Version }  </kbd>](${ anchorLink(linkableHeader(version.Version)) })`);
    
    while ( buttons.length ){
        
        const line = buttons
            .splice(0,6)
            .join('  \n');
    
        parts.push(line);
    }
    
    parts.push('</div>')
    
    parts.push('<br>\n<br>')
    
    
    for ( const version of page.versions ){
        
        const header = linkableHeader(version.Version);
        
        parts.push(`## ${ header }`)
        
        parts.push(`[\`Minecraft ${ version.Minecraft }\`][🎮 ${ version.Minecraft }]`)
        
        for ( const section in version.Sections ){
            
            parts.push('<br>')
            parts.push(`##### ${ section }`)
            
            for ( const point of version.Sections[section] )
                parts.push(`-   ${ toSection(point) }`)
        }
        
        parts.push('<br>\n<br>')
    }
    
    parts.push(`<!${ '-'.repeat(77) }>`)
    
    
    const mcVersions = new Set(page.versions.map(({ Minecraft }) => Minecraft)).values();
    
    for ( const version of mcVersions )
        parts.push(`[🎮 ${ version }]: ${ wikiLink(version) }`)
    
    
    const path = join(Output,`${ page.name }.md`);
    
    const content = '\n' 
        + parts.join('\n\n') 
        + '\n' ;
    
    await writeTextFile(path,content);
}


function linkableHeader ( header ){
    return header
        .trim()
        .split('.')
        .join(' . ')
}

function anchorLink ( header ){
    return '#' + header
        .trim()
        .replaceAll(/[^0-9a-z ]/ig,'')
        .replaceAll(' ','-')
}

function wikiLink ( version ){
    return `https://minecraft.fandom.com/wiki/Java_Edition_${ version }`
}

function toSection ( string ){
    return string
        .split('\n')
        .join('\n    ')
}
