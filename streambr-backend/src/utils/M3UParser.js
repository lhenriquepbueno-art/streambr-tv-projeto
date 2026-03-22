const fs = require('fs');
const readline = require('readline');

class M3UParser {
  /**
   * Parses a massive M3U file using streams to avoid memory issues.
   * @param {string} filePath Path to the .m3u file
   * @param {Function} onChannel Callback for each parsed channel
   * @param {Object} options Filtering options (e.g., allowedGroups)
   */
  static async parseStream(filePath, onChannel, options = {}) {
    return new Promise((resolve, reject) => {
      if (!fs.existsSync(filePath)) {
        return reject(new Error(`File not found: ${filePath}`));
      }

      const fileStream = fs.createReadStream(filePath);
      const rl = readline.createInterface({
        input: fileStream,
        crlfDelay: Infinity
      });

      let currentChannel = null;
      let lineCount = 0;

      rl.on('line', (line) => {
        lineCount++;
        
        if (line.startsWith('#EXTINF:')) {
          // Parse EXTINF line
          // Example: #EXTINF:-1 tvg-name="HBO 4K" tvg-logo="..." group-title="Canais | HBO",HBO 4K
          const nameMatch = line.match(/tvg-name="([^"]+)"/);
          const logoMatch = line.match(/tvg-logo="([^"]+)"/);
          const groupMatch = line.match(/group-title="([^"]+)"/);
          const displayNameMatch = line.match(/,(.+)$/);

          const groupTitle = groupMatch ? groupMatch[1] : 'Sem Categoria';
          
          // Filter by group-title if options provided
          // We only want groups that start with "Canais |" (Live TV)
          if (options.onlyCanais && !groupTitle.startsWith('Canais |')) {
            currentChannel = null;
            return;
          }

          currentChannel = {
            name: nameMatch ? nameMatch[1] : (displayNameMatch ? displayNameMatch[1].trim() : 'Canal Sem Nome'),
            logo: logoMatch ? logoMatch[1] : '',
            category: groupTitle,
            url: ''
          };
        } else if (line.trim() && !line.startsWith('#') && currentChannel) {
          // It's the URL line
          currentChannel.url = line.trim();
          onChannel(currentChannel);
          currentChannel = null;
        }

        // Show progress occasionally for very large files
        if (lineCount % 100000 === 0) {
          console.log(`[M3UParser] Processadas ${lineCount} linhas...`);
        }
      });

      rl.on('close', () => {
        console.log(`[M3UParser] Concluído. Total de linhas: ${lineCount}`);
        resolve();
      });

      rl.on('error', (err) => {
        reject(err);
      });
    });
  }
}

module.exports = M3UParser;
