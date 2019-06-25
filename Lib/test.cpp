// Created By ColdChip
#include <iostream>
#include <cstdint>
#include <cstring>
#include <vector>
#include <stdint.h>
#include <string>
#include <fstream>
#include <utility>
#include <sstream>
#include <unistd.h>
#include <limits.h>

using namespace std;

std::ifstream::pos_type filesize(const char* filename)
{
    std::ifstream in(filename, std::ifstream::ate | std::ifstream::binary);
    return in.tellg(); 
}

uint32_t HashString(unsigned char *str, int len)
{
    if (!str) return 0;

    unsigned char *n = (unsigned char *) str;
    uint32_t acc = 0x55555555;

    if (len == 0)
    {
        while (*n)
            acc = (acc >> 27) + (acc << 5) + *n++;
    } else
    {
        for (int i=0; i < len; i++)
        {
            acc = (acc >> 27) + (acc << 5) + *n++;
        }
    }
    return acc;

}

unsigned char *getA( string fileName, int *pSizeOut, bool bAddBasePath, bool bAutoDecompress)
{
    
        unsigned char * pData = NULL;


            //just try to load it from the default filesystem.  Should I create a FileSystemDefault for this to make it cleaner?
        
        FILE *fp = fopen(fileName.c_str(), "rb");
        if (!fp)
        {
            
            //not really an error, we might just want to know if a file exists
            //LogMsg("Warning: Proton FileManager says can't open %s", fileName.c_str());
            //file not found    
            if (!fp) return NULL;
        }

        fseek(fp, 0, SEEK_END);
        *pSizeOut = ftell(fp);
        fseek(fp, 0, SEEK_SET);

        pData = (unsigned char*)new unsigned char[( (*pSizeOut) +1)];
        if (!pData)
        {
            printf("Out of memory opening %s?", fileName.c_str());
            return 0;
        }
        //we add an extra null at the end to be nice, when loading text files this can be useful
        pData[*pSizeOut] = 0; 
        fread(pData, *pSizeOut, 1, fp);
        fclose(fp);

        return pData;
}

int main() {
    uint8_t *pData;
    int size = 0;
    const char filename[] = "items.dat";
    size = filesize(filename);
    pData = getA((string)filename, &size, false, false);

    printf("%i", HashString((unsigned char*)pData, size));
}